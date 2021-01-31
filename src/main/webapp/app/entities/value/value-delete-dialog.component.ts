import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IValue } from 'app/shared/model/value.model';
import { ValueService } from './value.service';

@Component({
  templateUrl: './value-delete-dialog.component.html',
})
export class ValueDeleteDialogComponent {
  value?: IValue;

  constructor(protected valueService: ValueService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.valueService.delete(id).subscribe(() => {
      this.eventManager.broadcast('valueListModification');
      this.activeModal.close();
    });
  }
}
