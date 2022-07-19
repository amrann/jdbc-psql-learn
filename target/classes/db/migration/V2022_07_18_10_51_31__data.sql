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