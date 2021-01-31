import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IValues } from 'app/shared/model/values.model';
import { ValuesService } from './values.service';

@Component({
  templateUrl: './values-delete-dialog.component.html',
})
export class ValuesDeleteDialogComponent {
  values?: IValues;

  constructor(protected valuesService: ValuesService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.valuesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('valuesListModification');
      this.activeModal.close();
    });
  }
}
