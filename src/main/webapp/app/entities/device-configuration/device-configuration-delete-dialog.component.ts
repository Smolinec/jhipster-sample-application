import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDeviceConfiguration } from 'app/shared/model/device-configuration.model';
import { DeviceConfigurationService } from './device-configuration.service';

@Component({
  templateUrl: './device-configuration-delete-dialog.component.html',
})
export class DeviceConfigurationDeleteDialogComponent {
  deviceConfiguration?: IDeviceConfiguration;

  constructor(
    protected deviceConfigurationService: DeviceConfigurationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deviceConfigurationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('deviceConfigurationListModification');
      this.activeModal.close();
    });
  }
}
