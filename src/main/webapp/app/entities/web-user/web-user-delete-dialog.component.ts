import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWebUser } from 'app/shared/model/web-user.model';
import { WebUserService } from './web-user.service';

@Component({
  templateUrl: './web-user-delete-dialog.component.html',
})
export class WebUserDeleteDialogComponent {
  webUser?: IWebUser;

  constructor(protected webUserService: WebUserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.webUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('webUserListModification');
      this.activeModal.close();
    });
  }
}
