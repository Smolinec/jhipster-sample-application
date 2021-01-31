import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'place',
        loadChildren: () => import('./place/place.module').then(m => m.JhipsterSampleApplicationPlaceModule),
      },
      {
        path: 'device',
        loadChildren: () => import('./device/device.module').then(m => m.JhipsterSampleApplicationDeviceModule),
      },
      {
        path: 'temperature',
        loadChildren: () => import('./temperature/temperature.module').then(m => m.JhipsterSampleApplicationTemperatureModule),
      },
      {
        path: 'value',
        loadChildren: () => import('./value/value.module').then(m => m.JhipsterSampleApplicationValueModule),
      },
      {
        path: 'web-user',
        loadChildren: () => import('./web-user/web-user.module').then(m => m.JhipsterSampleApplicationWebUserModule),
      },
      {
        path: 'push-notification-token',
        loadChildren: () =>
          import('./push-notification-token/push-notification-token.module').then(
            m => m.JhipsterSampleApplicationPushNotificationTokenModule
          ),
      },
      {
        path: 'values',
        loadChildren: () => import('./values/values.module').then(m => m.JhipsterSampleApplicationValuesModule),
      },
      {
        path: 'role',
        loadChildren: () => import('./role/role.module').then(m => m.JhipsterSampleApplicationRoleModule),
      },
      {
        path: 'application',
        loadChildren: () => import('./application/application.module').then(m => m.JhipsterSampleApplicationApplicationModule),
      },
      {
        path: 'device-profile',
        loadChildren: () => import('./device-profile/device-profile.module').then(m => m.JhipsterSampleApplicationDeviceProfileModule),
      },
      {
        path: 'device-configuration',
        loadChildren: () =>
          import('./device-configuration/device-configuration.module').then(m => m.JhipsterSampleApplicationDeviceConfigurationModule),
      },
      {
        path: 'sms-notification',
        loadChildren: () =>
          import('./sms-notification/sms-notification.module').then(m => m.JhipsterSampleApplicationSMSNotificationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class JhipsterSampleApplicationEntityModule {}
