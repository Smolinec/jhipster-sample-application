import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDeviceConfiguration, DeviceConfiguration } from 'app/shared/model/device-configuration.model';
import { DeviceConfigurationService } from './device-configuration.service';

@Component({
  selector: 'jhi-device-configuration-update',
  templateUrl: './device-configuration-update.component.html',
})
export class DeviceConfigurationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    primaryHostname: [],
    secondaryHostname: [],
    port: [],
  });

  constructor(
    protected deviceConfigurationService: DeviceConfigurationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceConfiguration }) => {
      this.updateForm(deviceConfiguration);
    });
  }

  updateForm(deviceConfiguration: IDeviceConfiguration): void {
    this.editForm.patchValue({
      id: deviceConfiguration.id,
      primaryHostname: deviceConfiguration.primaryHostname,
      secondaryHostname: deviceConfiguration.secondaryHostname,
      port: deviceConfiguration.port,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deviceConfiguration = this.createFromForm();
    if (deviceConfiguration.id !== undefined) {
      this.subscribeToSaveResponse(this.deviceConfigurationService.update(deviceConfiguration));
    } else {
      this.subscribeToSaveResponse(this.deviceConfigurationService.create(deviceConfiguration));
    }
  }

  private createFromForm(): IDeviceConfiguration {
    return {
      ...new DeviceConfiguration(),
      id: this.editForm.get(['id'])!.value,
      primaryHostname: this.editForm.get(['primaryHostname'])!.value,
      secondaryHostname: this.editForm.get(['secondaryHostname'])!.value,
      port: this.editForm.get(['port'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceConfiguration>>): void {
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
}
