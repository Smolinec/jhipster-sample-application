import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from './device.service';

@Component({
  templateUrl: './device-delete-dialog.component.html',
})
export class DeviceDeleteDialogComponent {
  device?: IDevice;

  constructor(protected deviceService: DeviceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deviceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('deviceListModification');
      this.activeModal.close();
    });
  }
}
