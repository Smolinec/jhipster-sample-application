import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IWebUser, WebUser } from 'app/shared/model/web-user.model';
import { WebUserService } from './web-user.service';
import { IPushNotificationToken } from 'app/shared/model/push-notification-token.model';
import { PushNotificationTokenService } from 'app/entities/push-notification-token/push-notification-token.service';

@Component({
  selector: 'jhi-web-user-update',
  templateUrl: './web-user-update.component.html',
})
export class WebUserUpdateComponent implements OnInit {
  isSaving = false;
  pushnotificationtokens: IPushNotificationToken[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    email: [],
    password: [],
    pushNotificationTokens: [],
  });

  constructor(
    protected webUserService: WebUserService,
    protected pushNotificationTokenService: PushNotificationTokenService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ webUser }) => {
      this.updateForm(webUser);

      this.pushNotificationTokenService
        .query()
        .subscribe((res: HttpResponse<IPushNotificationToken[]>) => (this.pushnotificationtokens = res.body || []));
    });
  }

  updateForm(webUser: IWebUser): void {
    this.editForm.patchValue({
      id: webUser.id,
      firstName: webUser.firstName,
      lastName: webUser.lastName,
      email: webUser.email,
      password: webUser.password,
      pushNotificationTokens: webUser.pushNotificationTokens,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const webUser = this.createFromForm();
    if (webUser.id !== undefined) {
      this.subscribeToSaveResponse(this.webUserService.update(webUser));
    } else {
      this.subscribeToSaveResponse(this.webUserService.create(webUser));
    }
  }

  private createFromForm(): IWebUser {
    return {
      ...new WebUser(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      password: this.editForm.get(['password'])!.value,
      pushNotificationTokens: this.editForm.get(['pushNotificationTokens'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWebUser>>): void {
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

  trackById(index: number, item: IPushNotificationToken): any {
    return item.id;
  }

  getSelected(selectedVals: IPushNotificationToken[], option: IPushNotificationToken): IPushNotificationToken {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
