import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITemperature, Temperature } from 'app/shared/model/temperature.model';
import { TemperatureService } from './temperature.service';
import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from 'app/entities/device/device.service';

@Component({
  selector: 'jhi-temperature-update',
  templateUrl: './temperature-update.component.html',
})
export class TemperatureUpdateComponent implements OnInit {
  isSaving = false;
  devices: IDevice[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    address: [],
    createTimestamp: [],
    lastUpdateTimestamp: [],
    device: [],
  });

  constructor(
    protected temperatureService: TemperatureService,
    protected deviceService: DeviceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ temperature }) => {
      if (!temperature.id) {
        const today = moment().startOf('day');
        temperature.createTimestamp = today;
        temperature.lastUpdateTimestamp = today;
      }

      this.updateForm(temperature);

      this.deviceService.query().subscribe((res: HttpResponse<IDevice[]>) => (this.devices = res.body || []));
    });
  }

  updateForm(temperature: ITemperature): void {
    this.editForm.patchValue({
      id: temperature.id,
      name: temperature.name,
      address: temperature.address,
      createTimestamp: temperature.createTimestamp ? temperature.createTimestamp.format(DATE_TIME_FORMAT) : null,
      lastUpdateTimestamp: temperature.lastUpdateTimestamp ? temperature.lastUpdateTimestamp.format(DATE_TIME_FORMAT) : null,
      device: temperature.device,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const temperature = this.createFromForm();
    if (temperature.id !== undefined) {
      this.subscribeToSaveResponse(this.temperatureService.update(temperature));
    } else {
      this.subscribeToSaveResponse(this.temperatureService.create(temperature));
    }
  }

  private createFromForm(): ITemperature {
    return {
      ...new Temperature(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      address: this.editForm.get(['address'])!.value,
      createTimestamp: this.editForm.get(['createTimestamp'])!.value
        ? moment(this.editForm.get(['createTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastUpdateTimestamp: this.editForm.get(['lastUpdateTimestamp'])!.value
        ? moment(this.editForm.get(['lastUpdateTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      device: this.editForm.get(['device'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITemperature>>): void {
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

  trackById(index: number, item: IDevice): any {
    return item.id;
  }
}
