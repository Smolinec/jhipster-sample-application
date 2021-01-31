import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeviceConfiguration } from 'app/shared/model/device-configuration.model';
import { DeviceConfigurationService } from './device-configuration.service';
import { DeviceConfigurationDeleteDialogComponent } from './device-configuration-delete-dialog.component';

@Component({
  selector: 'jhi-device-configuration',
  templateUrl: './device-configuration.component.html',
})
export class DeviceConfigurationComponent implements OnInit, OnDestroy {
  deviceConfigurations?: IDeviceConfiguration[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected deviceConfigurationService: DeviceConfigurationService,
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
      this.deviceConfigurationService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IDeviceConfiguration[]>) => (this.deviceConfigurations = res.body || []));
      return;
    }

    this.deviceConfigurationService
      .query()
      .subscribe((res: HttpResponse<IDeviceConfiguration[]>) => (this.deviceConfigurations = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDeviceConfigurations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDeviceConfiguration): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDeviceConfigurations(): void {
    this.eventSubscriber = this.eventManager.subscribe('deviceConfigurationListModification', () => this.loadAll());
  }

  delete(deviceConfiguration: IDeviceConfiguration): void {
    const modalRef = this.modalService.open(DeviceConfigurationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deviceConfiguration = deviceConfiguration;
  }
}
