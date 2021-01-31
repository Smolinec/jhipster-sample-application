import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { WebUserDetailComponent } from 'app/entities/web-user/web-user-detail.component';
import { WebUser } from 'app/shared/model/web-user.model';

describe('Component Tests', () => {
  describe('WebUser Management Detail Component', () => {
    let comp: WebUserDetailComponent;
    let fixture: ComponentFixture<WebUserDetailComponent>;
    const route = ({ data: of({ webUser: new WebUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [WebUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(WebUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WebUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load webUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.webUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
