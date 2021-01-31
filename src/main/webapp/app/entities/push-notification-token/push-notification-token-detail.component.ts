import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPushNotificationToken } from 'app/shared/model/push-notification-token.model';

@Component({
  selector: 'jhi-push-notification-token-detail',
  templateUrl: './push-notification-token-detail.component.html',
})
export class PushNotificationTokenDetailComponent implements OnInit {
  pushNotificationToken: IPushNotificationToken | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pushNotificationToken }) => (this.pushNotificationToken = pushNotificationToken));
  }

  previousState(): void {
    window.history.back();
  }
}
