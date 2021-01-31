import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { TemperatureComponent } from 'app/entities/temperature/temperature.component';
import { TemperatureService } from 'app/entities/temperature/temperature.service';
import { Temperature } from 'app/shared/model/temperature.model';

describe('Component Tests', () => {
  describe('Temperature Management Component', () => {
    let comp: TemperatureComponent;
    let fixture: ComponentFixture<TemperatureComponent>;
    let service: TemperatureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [TemperatureComponent],
      })
        .overrideTemplate(TemperatureComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TemperatureComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TemperatureService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Temperature(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.temperatures && comp.temperatures[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
