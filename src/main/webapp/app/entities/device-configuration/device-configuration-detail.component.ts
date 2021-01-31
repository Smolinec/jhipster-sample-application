import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeviceConfiguration } from 'app/shared/model/device-configuration.model';

@Component({
  selector: 'jhi-device-configuration-detail',
  templateUrl: './device-configuration-detail.component.html',
})
export class DeviceConfigurationDetailComponent implements OnInit {
  deviceConfiguration: IDeviceConfiguration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceConfiguration }) => (this.deviceConfiguration = deviceConfiguration));
  }

  previousState(): void {
    window.history.back();
  }
}
