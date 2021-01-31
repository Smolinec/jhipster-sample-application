import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDeviceProfile, DeviceProfile } from 'app/shared/model/device-profile.model';
import { DeviceProfileService } from './device-profile.service';
import { DeviceProfileComponent } from './device-profile.component';
import { DeviceProfileDetailComponent } from './device-profile-detail.component';
import { DeviceProfileUpdateComponent } from './device-profile-update.component';

@Injectable({ providedIn: 'root' })
export class DeviceProfileResolve implements Resolve<IDeviceProfile> {
  constructor(private service: DeviceProfileService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeviceProfile> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((deviceProfile: HttpResponse<DeviceProfile>) => {
          if (deviceProfile.body) {
            return of(deviceProfile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeviceProfile());
  }
}

export const deviceProfileRoute: Routes = [
  {
    path: '',
    component: DeviceProfileComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.deviceProfile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeviceProfileDetailComponent,
    resolve: {
      deviceProfile: DeviceProfileResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.deviceProfile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeviceProfileUpdateComponent,
    resolve: {
      deviceProfile: DeviceProfileResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.deviceProfile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeviceProfileUpdateComponent,
    resolve: {
      deviceProfile: DeviceProfileResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.deviceProfile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
