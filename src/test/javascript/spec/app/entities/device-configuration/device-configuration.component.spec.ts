import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { DeviceConfigurationComponent } from 'app/entities/device-configuration/device-configuration.component';
import { DeviceConfigurationService } from 'app/entities/device-configuration/device-configuration.service';
import { DeviceConfiguration } from 'app/shared/model/device-configuration.model';

describe('Component Tests', () => {
  describe('DeviceConfiguration Management Component', () => {
    let comp: DeviceConfigurationComponent;
    let fixture: ComponentFixture<DeviceConfigurationComponent>;
    let service: DeviceConfigurationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [DeviceConfigurationComponent],
      })
        .overrideTemplate(DeviceConfigurationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeviceConfigurationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DeviceConfigurationService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new DeviceConfiguration(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.deviceConfigurations && comp.deviceConfigurations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
