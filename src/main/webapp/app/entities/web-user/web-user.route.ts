import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IWebUser, WebUser } from 'app/shared/model/web-user.model';
import { WebUserService } from './web-user.service';
import { WebUserComponent } from './web-user.component';
import { WebUserDetailComponent } from './web-user-detail.component';
import { WebUserUpdateComponent } from './web-user-update.component';

@Injectable({ providedIn: 'root' })
export class WebUserResolve implements Resolve<IWebUser> {
  constructor(private service: WebUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWebUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((webUser: HttpResponse<WebUser>) => {
          if (webUser.body) {
            return of(webUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WebUser());
  }
}

export const webUserRoute: Routes = [
  {
    path: '',
    component: WebUserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.webUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WebUserDetailComponent,
    resolve: {
      webUser: WebUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.webUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WebUserUpdateComponent,
    resolve: {
      webUser: WebUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.webUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WebUserUpdateComponent,
    resolve: {
      webUser: WebUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.webUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
