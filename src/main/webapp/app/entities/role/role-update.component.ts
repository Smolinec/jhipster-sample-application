import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRole, Role } from 'app/shared/model/role.model';
import { RoleService } from './role.service';
import { IWebUser } from 'app/shared/model/web-user.model';
import { WebUserService } from 'app/entities/web-user/web-user.service';

@Component({
  selector: 'jhi-role-update',
  templateUrl: './role-update.component.html',
})
export class RoleUpdateComponent implements OnInit {
  isSaving = false;
  webusers: IWebUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    webUsers: [],
  });

  constructor(
    protected roleService: RoleService,
    protected webUserService: WebUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ role }) => {
      this.updateForm(role);

      this.webUserService.query().subscribe((res: HttpResponse<IWebUser[]>) => (this.webusers = res.body || []));
    });
  }

  updateForm(role: IRole): void {
    this.editForm.patchValue({
      id: role.id,
      name: role.name,
      webUsers: role.webUsers,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const role = this.createFromForm();
    if (role.id !== undefined) {
      this.subscribeToSaveResponse(this.roleService.update(role));
    } else {
      this.subscribeToSaveResponse(this.roleService.create(role));
    }
  }

  private createFromForm(): IRole {
    return {
      ...new Role(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      webUsers: this.editForm.get(['webUsers'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRole>>): void {
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

  trackById(index: number, item: IWebUser): any {
    return item.id;
  }

  getSelected(selectedVals: IWebUser[], option: IWebUser): IWebUser {
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
