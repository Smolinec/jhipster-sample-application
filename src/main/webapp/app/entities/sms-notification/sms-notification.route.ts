import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISMSNotification, SMSNotification } from 'app/shared/model/sms-notification.model';
import { SMSNotificationService } from './sms-notification.service';
import { SMSNotificationComponent } from './sms-notification.component';
import { SMSNotificationDetailComponent } from './sms-notification-detail.component';
import { SMSNotificationUpdateComponent } from './sms-notification-update.component';

@Injectable({ providedIn: 'root' })
export class SMSNotificationResolve implements Resolve<ISMSNotification> {
  constructor(private service: SMSNotificationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISMSNotification> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((sMSNotification: HttpResponse<SMSNotification>) => {
          if (sMSNotification.body) {
            return of(sMSNotification.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SMSNotification());
  }
}

export const sMSNotificationRoute: Routes = [
  {
    path: '',
    component: SMSNotificationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.sMSNotification.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SMSNotificationDetailComponent,
    resolve: {
      sMSNotification: SMSNotificationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.sMSNotification.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SMSNotificationUpdateComponent,
    resolve: {
      sMSNotification: SMSNotificationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.sMSNotification.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SMSNotificationUpdateComponent,
    resolve: {
      sMSNotification: SMSNotificationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.sMSNotification.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
