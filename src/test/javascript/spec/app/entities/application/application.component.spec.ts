import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { ApplicationComponent } from 'app/entities/application/application.component';
import { ApplicationService } from 'app/entities/application/application.service';
import { Application } from 'app/shared/model/application.model';

describe('Component Tests', () => {
  describe('Application Management Component', () => {
    let comp: ApplicationComponent;
    let fixture: ComponentFixture<ApplicationComponent>;
    let service: ApplicationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [ApplicationComponent],
      })
        .overrideTemplate(ApplicationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApplicationService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Application(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.applications && comp.applications[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
