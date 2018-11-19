import {NgModule} from "@angular/core";
import {FileSizePipe} from "./pipe/file-size.pipe";
import {LinkComponent} from "./link/link.component";
import {CommonModule} from "@angular/common";
import {MediaPosterComponent} from "./image/media-poster.component";
import {SharedModule} from "primeng/components/common/shared";
import {DataTableModule} from "primeng/components/datatable/datatable";
import {FileQualityPipe} from "./pipe/file-quality.pipe";
import {FileCodecPipe} from "./pipe/file-codec.pipe";
import {ConfirmationService} from "primeng/components/common/api";
import {ConfirmDialogModule} from "primeng/components/confirmdialog/confirmdialog";
import {FilterGenreComponent} from "./filters/genre/filter-genre.component";
import {FilterOrderComponent} from "./filters/order/filter-order.component";

@NgModule({
  imports: [
    CommonModule,
    DataTableModule,
    SharedModule,
    ConfirmDialogModule
  ],
  declarations: [
    FileSizePipe,
    FileQualityPipe,
    FileCodecPipe,
    LinkComponent,
    MediaPosterComponent,
    FilterGenreComponent,
    FilterOrderComponent
  ],
  exports: [
    FileSizePipe,
    FileQualityPipe,
    FileCodecPipe,
    LinkComponent,
    MediaPosterComponent,
    FilterGenreComponent,
    FilterOrderComponent
  ],
  providers: [
    ConfirmationService
  ]
})

export class AppSharedModule {
  static forRoot() {
    return {
      ngModule: AppSharedModule
    };
  }
}
