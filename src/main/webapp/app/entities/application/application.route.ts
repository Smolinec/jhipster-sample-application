import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IApplication, Application } from 'app/shared/model/application.model';
import { ApplicationService } from './application.service';
import { ApplicationComponent } from './application.component';
import { ApplicationDetailComponent } from './application-detail.component';
import { ApplicationUpdateComponent } from './application-update.component';

@Injectable({ providedIn: 'root' })
export class ApplicationResolve implements Resolve<IApplication> {
  constructor(private service: ApplicationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApplication> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((application: HttpResponse<Application>) => {
          if (application.body) {
            return of(application.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Application());
  }
}

export const applicationRoute: Routes = [
  {
    path: '',
    component: ApplicationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.application.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplicationDetailComponent,
    resolve: {
      application: ApplicationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.application.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplicationUpdateComponent,
    resolve: {
      application: ApplicationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.application.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplicationUpdateComponent,
    resolve: {
      application: ApplicationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.application.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
