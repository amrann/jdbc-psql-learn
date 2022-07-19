alter table perpustakaan.buku
add constraint fk_penerbit_id foreign key (penerbit_id)
references perpustakaan.penerbit(id) on update cascade on delete set null;

alter table perpustakaan.transaksi
add constraint fk_anggota_id foreign key (anggota_id)
references perpustakaan.anggota(id) on update cascade on delete cascade;

alter table perpustakaan.transaksi_detail
add constraint fk_buku_id foreign key (buku_id)
references perpustakaan.buku(id) on update cascade on delete cascade;

alter table perpustakaan.transaksi_detail
add constraint fk_transaksi_id foreign key (transaksi_id)
references perpustakaan.transaksi(id) on update cascade on delete cascade;