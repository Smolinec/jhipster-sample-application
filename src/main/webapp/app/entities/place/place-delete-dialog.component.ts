import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlace } from 'app/shared/model/place.model';
import { PlaceService } from './place.service';

@Component({
  templateUrl: './place-delete-dialog.component.html',
})
export class PlaceDeleteDialogComponent {
  place?: IPlace;

  constructor(protected placeService: PlaceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.placeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('placeListModification');
      this.activeModal.close();
    });
  }
}
