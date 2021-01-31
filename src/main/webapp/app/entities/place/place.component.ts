import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlace } from 'app/shared/model/place.model';
import { PlaceService } from './place.service';
import { PlaceDeleteDialogComponent } from './place-delete-dialog.component';

@Component({
  selector: 'jhi-place',
  templateUrl: './place.component.html',
})
export class PlaceComponent implements OnInit, OnDestroy {
  places?: IPlace[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected placeService: PlaceService,
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
      this.placeService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IPlace[]>) => (this.places = res.body || []));
      return;
    }

    this.placeService.query().subscribe((res: HttpResponse<IPlace[]>) => (this.places = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPlaces();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPlace): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPlaces(): void {
    this.eventSubscriber = this.eventManager.subscribe('placeListModification', () => this.loadAll());
  }

  delete(place: IPlace): void {
    const modalRef = this.modalService.open(PlaceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.place = place;
  }
}
