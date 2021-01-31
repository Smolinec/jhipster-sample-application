import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISMSNotification } from 'app/shared/model/sms-notification.model';
import { SMSNotificationService } from './sms-notification.service';

@Component({
  templateUrl: './sms-notification-delete-dialog.component.html',
})
export class SMSNotificationDeleteDialogComponent {
  sMSNotification?: ISMSNotification;

  constructor(
    protected sMSNotificationService: SMSNotificationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sMSNotificationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('sMSNotificationListModification');
      this.activeModal.close();
    });
  }
}
