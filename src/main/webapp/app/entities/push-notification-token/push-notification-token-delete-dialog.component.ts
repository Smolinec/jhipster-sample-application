import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPushNotificationToken } from 'app/shared/model/push-notification-token.model';
import { PushNotificationTokenService } from './push-notification-token.service';

@Component({
  templateUrl: './push-notification-token-delete-dialog.component.html',
})
export class PushNotificationTokenDeleteDialogComponent {
  pushNotificationToken?: IPushNotificationToken;

  constructor(
    protected pushNotificationTokenService: PushNotificationTokenService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pushNotificationTokenService.delete(id).subscribe(() => {
      this.eventManager.broadcast('pushNotificationTokenListModification');
      this.activeModal.close();
    });
  }
}
