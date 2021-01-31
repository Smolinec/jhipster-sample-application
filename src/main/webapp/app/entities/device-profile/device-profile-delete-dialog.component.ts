import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDeviceProfile } from 'app/shared/model/device-profile.model';
import { DeviceProfileService } from './device-profile.service';

@Component({
  templateUrl: './device-profile-delete-dialog.component.html',
})
export class DeviceProfileDeleteDialogComponent {
  deviceProfile?: IDeviceProfile;

  constructor(
    protected deviceProfileService: DeviceProfileService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deviceProfileService.delete(id).subscribe(() => {
      this.eventManager.broadcast('deviceProfileListModification');
      this.activeModal.close();
    });
  }
}
