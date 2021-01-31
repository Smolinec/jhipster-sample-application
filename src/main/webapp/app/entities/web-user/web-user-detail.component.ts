import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWebUser } from 'app/shared/model/web-user.model';

@Component({
  selector: 'jhi-web-user-detail',
  templateUrl: './web-user-detail.component.html',
})
export class WebUserDetailComponent implements OnInit {
  webUser: IWebUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ webUser }) => (this.webUser = webUser));
  }

  previousState(): void {
    window.history.back();
  }
}
