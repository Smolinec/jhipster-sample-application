import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { WebUserComponent } from 'app/entities/web-user/web-user.component';
import { WebUserService } from 'app/entities/web-user/web-user.service';
import { WebUser } from 'app/shared/model/web-user.model';

describe('Component Tests', () => {
  describe('WebUser Management Component', () => {
    let comp: WebUserComponent;
    let fixture: ComponentFixture<WebUserComponent>;
    let service: WebUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [WebUserComponent],
      })
        .overrideTemplate(WebUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WebUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WebUserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new WebUser(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.webUsers && comp.webUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
