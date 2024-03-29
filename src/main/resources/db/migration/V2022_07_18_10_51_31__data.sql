insert into perpustakaan.penerbit(id, nama , alamat)
values	('001', 'Informatika', 'Jakarta'),
		('002', 'Erlangga', 'Bandung');

insert into perpustakaan.buku(id, nama, isbn, penerbit_id)
values	('001', 'Pemograman Java', '0000-000-0000', '001'),
		('002', 'LKS Pelajaran Bahasa Indonesia', '0001-000-0001', '002'),
		('003', 'Pemograman Java 2', '0000-000-0000', '001');

insert into perpustakaan.anggota(id, nomor_ktp, nama, alamat)
values	('001', '6204123434', 'Dimas Maryanto', 'Bandung'),
		('002', '6204123435', 'Meran', 'Maros'),
		('003', '6204123436', 'Sopyan', 'Bulukumba');

insert into perpustakaan.penulis(id, nama, alamat)
values ('001', 'Hera Kusnadi', 'Bandung'),
       ('002', 'Aksara Tari', 'Makassar'),
       ('003', 'Henry Pena', 'Jakarta Pusat');

insert into perpustakaan.penulis_buku(buku_id, penulis_id)
values ('001', '001'),
       ('001', '002'),
       ('002', '003');


-- #INHERITANCE
insert into bank.nasabah(cif, nama, npwp, siup, ktp, foto, type, created_date, created_by)
values ('001', 'Meran Maruusy', null, null, '7368452341859993', null, 1, now(), 'migration'),
       ('002', 'PT. Kuat Kita Bersama', '3465784567', '3456', null, null, 2, now(), 'migration');

insert into bank.nasabah_perorangan(cif, nama, ktp, foto, created_date, created_by)
values ('001', 'Meran Maruusy', '7368452341859993', null, now(), 'migration');

insert into bank.nasabah_badanusaha(cif, nama, npwp, siup, created_date, created_by)
values ('002', 'PT. Kuat Kita Bersama', '3465784567', '4578', now(), 'migration');