import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { SMSNotificationComponent } from './sms-notification.component';
import { SMSNotificationDetailComponent } from './sms-notification-detail.component';
import { SMSNotificationUpdateComponent } from './sms-notification-update.component';
import { SMSNotificationDeleteDialogComponent } from './sms-notification-delete-dialog.component';
import { sMSNotificationRoute } from './sms-notification.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(sMSNotificationRoute)],
  declarations: [
    SMSNotificationComponent,
    SMSNotificationDetailComponent,
    SMSNotificationUpdateComponent,
    SMSNotificationDeleteDialogComponent,
  ],
  entryComponents: [SMSNotificationDeleteDialogComponent],
})
export class JhipsterSampleApplicationSMSNotificationModule {}
