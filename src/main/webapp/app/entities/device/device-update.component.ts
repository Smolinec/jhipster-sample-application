import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDevice, Device } from 'app/shared/model/device.model';
import { DeviceService } from './device.service';
import { IPlace } from 'app/shared/model/place.model';
import { PlaceService } from 'app/entities/place/place.service';

@Component({
  selector: 'jhi-device-update',
  templateUrl: './device-update.component.html',
})
export class DeviceUpdateComponent implements OnInit {
  isSaving = false;
  places: IPlace[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [],
    appVersion: [],
    idUpdated: [],
    place: [],
  });

  constructor(
    protected deviceService: DeviceService,
    protected placeService: PlaceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ device }) => {
      this.updateForm(device);

      this.placeService.query().subscribe((res: HttpResponse<IPlace[]>) => (this.places = res.body || []));
    });
  }

  updateForm(device: IDevice): void {
    this.editForm.patchValue({
      id: device.id,
      uuid: device.uuid,
      appVersion: device.appVersion,
      idUpdated: device.idUpdated,
      place: device.place,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const device = this.createFromForm();
    if (device.id !== undefined) {
      this.subscribeToSaveResponse(this.deviceService.update(device));
    } else {
      this.subscribeToSaveResponse(this.deviceService.create(device));
    }
  }

  private createFromForm(): IDevice {
    return {
      ...new Device(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      appVersion: this.editForm.get(['appVersion'])!.value,
      idUpdated: this.editForm.get(['idUpdated'])!.value,
      place: this.editForm.get(['place'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IPlace): any {
    return item.id;
  }
}
