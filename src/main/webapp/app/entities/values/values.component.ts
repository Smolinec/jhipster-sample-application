import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IValues } from 'app/shared/model/values.model';
import { ValuesService } from './values.service';
import { ValuesDeleteDialogComponent } from './values-delete-dialog.component';

@Component({
  selector: 'jhi-values',
  templateUrl: './values.component.html',
})
export class ValuesComponent implements OnInit, OnDestroy {
  values?: IValues[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected valuesService: ValuesService,
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
      this.valuesService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IValues[]>) => (this.values = res.body || []));
      return;
    }

    this.valuesService.query().subscribe((res: HttpResponse<IValues[]>) => (this.values = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInValues();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IValues): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInValues(): void {
    this.eventSubscriber = this.eventManager.subscribe('valuesListModification', () => this.loadAll());
  }

  delete(values: IValues): void {
    const modalRef = this.modalService.open(ValuesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.values = values;
  }
}
