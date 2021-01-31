import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from './device.service';
import { DeviceDeleteDialogComponent } from './device-delete-dialog.component';

@Component({
  selector: 'jhi-device',
  templateUrl: './device.component.html',
})
export class DeviceComponent implements OnInit, OnDestroy {
  devices?: IDevice[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected deviceService: DeviceService,
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
      this.deviceService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IDevice[]>) => (this.devices = res.body || []));
      return;
    }

    this.deviceService.query().subscribe((res: HttpResponse<IDevice[]>) => (this.devices = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDevices();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDevice): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDevices(): void {
    this.eventSubscriber = this.eventManager.subscribe('deviceListModification', () => this.loadAll());
  }

  delete(device: IDevice): void {
    const modalRef = this.modalService.open(DeviceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.device = device;
  }
}
