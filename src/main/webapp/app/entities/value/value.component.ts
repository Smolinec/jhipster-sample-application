import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IValue } from 'app/shared/model/value.model';
import { ValueService } from './value.service';
import { ValueDeleteDialogComponent } from './value-delete-dialog.component';

@Component({
  selector: 'jhi-value',
  templateUrl: './value.component.html',
})
export class ValueComponent implements OnInit, OnDestroy {
  values?: IValue[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected valueService: ValueService,
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
      this.valueService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IValue[]>) => (this.values = res.body || []));
      return;
    }

    this.valueService.query().subscribe((res: HttpResponse<IValue[]>) => (this.values = res.body || []));
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

  trackId(index: number, item: IValue): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInValues(): void {
    this.eventSubscriber = this.eventManager.subscribe('valueListModification', () => this.loadAll());
  }

  delete(value: IValue): void {
    const modalRef = this.modalService.open(ValueDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.value = value;
  }
}
