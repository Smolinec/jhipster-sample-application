import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IValues, Values } from 'app/shared/model/values.model';
import { ValuesService } from './values.service';
import { ValuesComponent } from './values.component';
import { ValuesDetailComponent } from './values-detail.component';
import { ValuesUpdateComponent } from './values-update.component';

@Injectable({ providedIn: 'root' })
export class ValuesResolve implements Resolve<IValues> {
  constructor(private service: ValuesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IValues> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((values: HttpResponse<Values>) => {
          if (values.body) {
            return of(values.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Values());
  }
}

export const valuesRoute: Routes = [
  {
    path: '',
    component: ValuesComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.values.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ValuesDetailComponent,
    resolve: {
      values: ValuesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.values.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ValuesUpdateComponent,
    resolve: {
      values: ValuesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.values.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ValuesUpdateComponent,
    resolve: {
      values: ValuesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.values.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
