import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPushNotificationToken, PushNotificationToken } from 'app/shared/model/push-notification-token.model';
import { PushNotificationTokenService } from './push-notification-token.service';
import { PushNotificationTokenComponent } from './push-notification-token.component';
import { PushNotificationTokenDetailComponent } from './push-notification-token-detail.component';
import { PushNotificationTokenUpdateComponent } from './push-notification-token-update.component';

@Injectable({ providedIn: 'root' })
export class PushNotificationTokenResolve implements Resolve<IPushNotificationToken> {
  constructor(private service: PushNotificationTokenService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPushNotificationToken> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pushNotificationToken: HttpResponse<PushNotificationToken>) => {
          if (pushNotificationToken.body) {
            return of(pushNotificationToken.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PushNotificationToken());
  }
}

export const pushNotificationTokenRoute: Routes = [
  {
    path: '',
    component: PushNotificationTokenComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.pushNotificationToken.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PushNotificationTokenDetailComponent,
    resolve: {
      pushNotificationToken: PushNotificationTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.pushNotificationToken.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PushNotificationTokenUpdateComponent,
    resolve: {
      pushNotificationToken: PushNotificationTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.pushNotificationToken.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PushNotificationTokenUpdateComponent,
    resolve: {
      pushNotificationToken: PushNotificationTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.pushNotificationToken.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
