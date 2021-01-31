import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPushNotificationToken } from 'app/shared/model/push-notification-token.model';
import { PushNotificationTokenService } from './push-notification-token.service';
import { PushNotificationTokenDeleteDialogComponent } from './push-notification-token-delete-dialog.component';

@Component({
  selector: 'jhi-push-notification-token',
  templateUrl: './push-notification-token.component.html',
})
export class PushNotificationTokenComponent implements OnInit, OnDestroy {
  pushNotificationTokens?: IPushNotificationToken[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected pushNotificationTokenService: PushNotificationTokenService,
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
      this.pushNotificationTokenService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IPushNotificationToken[]>) => (this.pushNotificationTokens = res.body || []));
      return;
    }

    this.pushNotificationTokenService
      .query()
      .subscribe((res: HttpResponse<IPushNotificationToken[]>) => (this.pushNotificationTokens = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPushNotificationTokens();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPushNotificationToken): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPushNotificationTokens(): void {
    this.eventSubscriber = this.eventManager.subscribe('pushNotificationTokenListModification', () => this.loadAll());
  }

  delete(pushNotificationToken: IPushNotificationToken): void {
    const modalRef = this.modalService.open(PushNotificationTokenDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pushNotificationToken = pushNotificationToken;
  }
}
