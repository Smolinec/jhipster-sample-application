import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISMSNotification } from 'app/shared/model/sms-notification.model';

@Component({
  selector: 'jhi-sms-notification-detail',
  templateUrl: './sms-notification-detail.component.html',
})
export class SMSNotificationDetailComponent implements OnInit {
  sMSNotification: ISMSNotification | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sMSNotification }) => (this.sMSNotification = sMSNotification));
  }

  previousState(): void {
    window.history.back();
  }
}
