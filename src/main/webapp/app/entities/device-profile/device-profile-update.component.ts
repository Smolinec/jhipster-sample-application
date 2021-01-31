import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDeviceProfile, DeviceProfile } from 'app/shared/model/device-profile.model';
import { DeviceProfileService } from './device-profile.service';
import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from 'app/entities/device/device.service';

@Component({
  selector: 'jhi-device-profile-update',
  templateUrl: './device-profile-update.component.html',
})
export class DeviceProfileUpdateComponent implements OnInit {
  isSaving = false;
  devices: IDevice[] = [];

  editForm = this.fb.group({
    id: [],
    devices: [],
  });

  constructor(
    protected deviceProfileService: DeviceProfileService,
    protected deviceService: DeviceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceProfile }) => {
      this.updateForm(deviceProfile);

      this.deviceService.query().subscribe((res: HttpResponse<IDevice[]>) => (this.devices = res.body || []));
    });
  }

  updateForm(deviceProfile: IDeviceProfile): void {
    this.editForm.patchValue({
      id: deviceProfile.id,
      devices: deviceProfile.devices,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deviceProfile = this.createFromForm();
    if (deviceProfile.id !== undefined) {
      this.subscribeToSaveResponse(this.deviceProfileService.update(deviceProfile));
    } else {
      this.subscribeToSaveResponse(this.deviceProfileService.create(deviceProfile));
    }
  }

  private createFromForm(): IDeviceProfile {
    return {
      ...new DeviceProfile(),
      id: this.editForm.get(['id'])!.value,
      devices: this.editForm.get(['devices'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceProfile>>): void {
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

  getSelected(selectedVals: IDevice[], option: IDevice): IDevice {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
