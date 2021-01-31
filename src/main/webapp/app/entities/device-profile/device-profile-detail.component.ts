import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeviceProfile } from 'app/shared/model/device-profile.model';

@Component({
  selector: 'jhi-device-profile-detail',
  templateUrl: './device-profile-detail.component.html',
})
export class DeviceProfileDetailComponent implements OnInit {
  deviceProfile: IDeviceProfile | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceProfile }) => (this.deviceProfile = deviceProfile));
  }

  previousState(): void {
    window.history.back();
  }
}
