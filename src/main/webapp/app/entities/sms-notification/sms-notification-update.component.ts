import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ISMSNotification, SMSNotification } from 'app/shared/model/sms-notification.model';
import { SMSNotificationService } from './sms-notification.service';

@Component({
  selector: 'jhi-sms-notification-update',
  templateUrl: './sms-notification-update.component.html',
})
export class SMSNotificationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    telNumber: [],
    message: [],
    createdTimestamp: [],
    uuidDevice: [],
    isSending: [],
    sendingTimestamp: [],
    isSend: [],
    sendTimestamp: [],
    alertType: [],
    featureSend: [],
  });

  constructor(
    protected sMSNotificationService: SMSNotificationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sMSNotification }) => {
      if (!sMSNotification.id) {
        const today = moment().startOf('day');
        sMSNotification.createdTimestamp = today;
        sMSNotification.sendingTimestamp = today;
        sMSNotification.sendTimestamp = today;
        sMSNotification.featureSend = today;
      }

      this.updateForm(sMSNotification);
    });
  }

  updateForm(sMSNotification: ISMSNotification): void {
    this.editForm.patchValue({
      id: sMSNotification.id,
      telNumber: sMSNotification.telNumber,
      message: sMSNotification.message,
      createdTimestamp: sMSNotification.createdTimestamp ? sMSNotification.createdTimestamp.format(DATE_TIME_FORMAT) : null,
      uuidDevice: sMSNotification.uuidDevice,
      isSending: sMSNotification.isSending,
      sendingTimestamp: sMSNotification.sendingTimestamp ? sMSNotification.sendingTimestamp.format(DATE_TIME_FORMAT) : null,
      isSend: sMSNotification.isSend,
      sendTimestamp: sMSNotification.sendTimestamp ? sMSNotification.sendTimestamp.format(DATE_TIME_FORMAT) : null,
      alertType: sMSNotification.alertType,
      featureSend: sMSNotification.featureSend ? sMSNotification.featureSend.format(DATE_TIME_FORMAT) : null,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sMSNotification = this.createFromForm();
    if (sMSNotification.id !== undefined) {
      this.subscribeToSaveResponse(this.sMSNotificationService.update(sMSNotification));
    } else {
      this.subscribeToSaveResponse(this.sMSNotificationService.create(sMSNotification));
    }
  }

  private createFromForm(): ISMSNotification {
    return {
      ...new SMSNotification(),
      id: this.editForm.get(['id'])!.value,
      telNumber: this.editForm.get(['telNumber'])!.value,
      message: this.editForm.get(['message'])!.value,
      createdTimestamp: this.editForm.get(['createdTimestamp'])!.value
        ? moment(this.editForm.get(['createdTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      uuidDevice: this.editForm.get(['uuidDevice'])!.value,
      isSending: this.editForm.get(['isSending'])!.value,
      sendingTimestamp: this.editForm.get(['sendingTimestamp'])!.value
        ? moment(this.editForm.get(['sendingTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      isSend: this.editForm.get(['isSend'])!.value,
      sendTimestamp: this.editForm.get(['sendTimestamp'])!.value
        ? moment(this.editForm.get(['sendTimestamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      alertType: this.editForm.get(['alertType'])!.value,
      featureSend: this.editForm.get(['featureSend'])!.value
        ? moment(this.editForm.get(['featureSend'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISMSNotification>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
