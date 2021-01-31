import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPushNotificationToken, PushNotificationToken } from 'app/shared/model/push-notification-token.model';
import { PushNotificationTokenService } from './push-notification-token.service';

@Component({
  selector: 'jhi-push-notification-token-update',
  templateUrl: './push-notification-token-update.component.html',
})
export class PushNotificationTokenUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    token: [],
    timestamp: [],
  });

  constructor(
    protected pushNotificationTokenService: PushNotificationTokenService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pushNotificationToken }) => {
      if (!pushNotificationToken.id) {
        const today = moment().startOf('day');
        pushNotificationToken.timestamp = today;
      }

      this.updateForm(pushNotificationToken);
    });
  }

  updateForm(pushNotificationToken: IPushNotificationToken): void {
    this.editForm.patchValue({
      id: pushNotificationToken.id,
      token: pushNotificationToken.token,
      timestamp: pushNotificationToken.timestamp ? pushNotificationToken.timestamp.format(DATE_TIME_FORMAT) : null,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pushNotificationToken = this.createFromForm();
    if (pushNotificationToken.id !== undefined) {
      this.subscribeToSaveResponse(this.pushNotificationTokenService.update(pushNotificationToken));
    } else {
      this.subscribeToSaveResponse(this.pushNotificationTokenService.create(pushNotificationToken));
    }
  }

  private createFromForm(): IPushNotificationToken {
    return {
      ...new PushNotificationToken(),
      id: this.editForm.get(['id'])!.value,
      token: this.editForm.get(['token'])!.value,
      timestamp: this.editForm.get(['timestamp'])!.value ? moment(this.editForm.get(['timestamp'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPushNotificationToken>>): void {
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
