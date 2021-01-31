import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IValue, Value } from 'app/shared/model/value.model';
import { ValueService } from './value.service';
import { ValueComponent } from './value.component';
import { ValueDetailComponent } from './value-detail.component';
import { ValueUpdateComponent } from './value-update.component';

@Injectable({ providedIn: 'root' })
export class ValueResolve implements Resolve<IValue> {
  constructor(private service: ValueService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IValue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((value: HttpResponse<Value>) => {
          if (value.body) {
            return of(value.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Value());
  }
}

export const valueRoute: Routes = [
  {
    path: '',
    component: ValueComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.value.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ValueDetailComponent,
    resolve: {
      value: ValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.value.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ValueUpdateComponent,
    resolve: {
      value: ValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.value.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ValueUpdateComponent,
    resolve: {
      value: ValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.value.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
