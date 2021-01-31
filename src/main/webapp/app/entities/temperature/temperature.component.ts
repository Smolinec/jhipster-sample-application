import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITemperature } from 'app/shared/model/temperature.model';
import { TemperatureService } from './temperature.service';
import { TemperatureDeleteDialogComponent } from './temperature-delete-dialog.component';

@Component({
  selector: 'jhi-temperature',
  templateUrl: './temperature.component.html',
})
export class TemperatureComponent implements OnInit, OnDestroy {
  temperatures?: ITemperature[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected temperatureService: TemperatureService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.temperatureService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<ITemperature[]>) => (this.temperatures = res.body || []));
      return;
    }

    this.temperatureService.query().subscribe((res: HttpResponse<ITemperature[]>) => (this.temperatures = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTemperatures();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITemperature): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTemperatures(): void {
    this.eventSubscriber = this.eventManager.subscribe('temperatureListModification', () => this.loadAll());
  }

  delete(temperature: ITemperature): void {
    const modalRef = this.modalService.open(TemperatureDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.temperature = temperature;
  }
}
