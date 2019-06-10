import { NgModule } from '@angular/core';

import { InvitationsSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [InvitationsSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [InvitationsSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class InvitationsSharedCommonModule {}
