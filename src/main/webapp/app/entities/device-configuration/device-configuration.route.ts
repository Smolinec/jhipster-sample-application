import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDeviceConfiguration, DeviceConfiguration } from 'app/shared/model/device-configuration.model';
import { DeviceConfigurationService } from './device-configuration.service';
import { DeviceConfigurationComponent } from './device-configuration.component';
import { DeviceConfigurationDetailComponent } from './device-configuration-detail.component';
import { DeviceConfigurationUpdateComponent } from './device-configuration-update.component';

@Injectable({ providedIn: 'root' })
export class DeviceConfigurationResolve implements Resolve<IDeviceConfiguration> {
  constructor(private service: DeviceConfigurationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeviceConfiguration> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((deviceConfiguration: HttpResponse<DeviceConfiguration>) => {
          if (deviceConfiguration.body) {
            return of(deviceConfiguration.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeviceConfiguration());
  }
}

export const deviceConfigurationRoute: Routes = [
  {
    path: '',
    component: DeviceConfigurationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.deviceConfiguration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeviceConfigurationDetailComponent,
    resolve: {
      deviceConfiguration: DeviceConfigurationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.deviceConfiguration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeviceConfigurationUpdateComponent,
    resolve: {
      deviceConfiguration: DeviceConfigurationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.deviceConfiguration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeviceConfigurationUpdateComponent,
    resolve: {
      deviceConfiguration: DeviceConfigurationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.deviceConfiguration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
