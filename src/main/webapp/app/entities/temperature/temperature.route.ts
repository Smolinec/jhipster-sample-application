import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITemperature, Temperature } from 'app/shared/model/temperature.model';
import { TemperatureService } from './temperature.service';
import { TemperatureComponent } from './temperature.component';
import { TemperatureDetailComponent } from './temperature-detail.component';
import { TemperatureUpdateComponent } from './temperature-update.component';

@Injectable({ providedIn: 'root' })
export class TemperatureResolve implements Resolve<ITemperature> {
  constructor(private service: TemperatureService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITemperature> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((temperature: HttpResponse<Temperature>) => {
          if (temperature.body) {
            return of(temperature.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Temperature());
  }
}

export const temperatureRoute: Routes = [
  {
    path: '',
    component: TemperatureComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.temperature.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TemperatureDetailComponent,
    resolve: {
      temperature: TemperatureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.temperature.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TemperatureUpdateComponent,
    resolve: {
      temperature: TemperatureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.temperature.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TemperatureUpdateComponent,
    resolve: {
      temperature: TemperatureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.temperature.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
