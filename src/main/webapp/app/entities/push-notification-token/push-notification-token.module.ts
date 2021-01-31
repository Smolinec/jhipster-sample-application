import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { PushNotificationTokenComponent } from './push-notification-token.component';
import { PushNotificationTokenDetailComponent } from './push-notification-token-detail.component';
import { PushNotificationTokenUpdateComponent } from './push-notification-token-update.component';
import { PushNotificationTokenDeleteDialogComponent } from './push-notification-token-delete-dialog.component';
import { pushNotificationTokenRoute } from './push-notification-token.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(pushNotificationTokenRoute)],
  declarations: [
    PushNotificationTokenComponent,
    PushNotificationTokenDetailComponent,
    PushNotificationTokenUpdateComponent,
    PushNotificationTokenDeleteDialogComponent,
  ],
  entryComponents: [PushNotificationTokenDeleteDialogComponent],
})
export class JhipsterSampleApplicationPushNotificationTokenModule {}
