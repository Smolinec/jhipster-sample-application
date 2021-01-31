import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITemperature } from 'app/shared/model/temperature.model';
import { TemperatureService } from './temperature.service';

@Component({
  templateUrl: './temperature-delete-dialog.component.html',
})
export class TemperatureDeleteDialogComponent {
  temperature?: ITemperature;

  constructor(
    protected temperatureService: TemperatureService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.temperatureService.delete(id).subscribe(() => {
      this.eventManager.broadcast('temperatureListModification');
      this.activeModal.close();
    });
  }
}
