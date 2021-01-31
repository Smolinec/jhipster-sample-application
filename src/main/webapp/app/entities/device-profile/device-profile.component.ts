import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeviceProfile } from 'app/shared/model/device-profile.model';
import { DeviceProfileService } from './device-profile.service';
import { DeviceProfileDeleteDialogComponent } from './device-profile-delete-dialog.component';

@Component({
  selector: 'jhi-device-profile',
  templateUrl: './device-profile.component.html',
})
export class DeviceProfileComponent implements OnInit, OnDestroy {
  deviceProfiles?: IDeviceProfile[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected deviceProfileService: DeviceProfileService,
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
      this.deviceProfileService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IDeviceProfile[]>) => (this.deviceProfiles = res.body || []));
      return;
    }

    this.deviceProfileService.query().subscribe((res: HttpResponse<IDeviceProfile[]>) => (this.deviceProfiles = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDeviceProfiles();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDeviceProfile): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDeviceProfiles(): void {
    this.eventSubscriber = this.eventManager.subscribe('deviceProfileListModification', () => this.loadAll());
  }

  delete(deviceProfile: IDeviceProfile): void {
    const modalRef = this.modalService.open(DeviceProfileDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deviceProfile = deviceProfile;
  }
}
