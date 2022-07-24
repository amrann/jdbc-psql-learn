create table perpustakaan.buku (
	id 			character varying(64) not null primary key default gen_random_uuid(),
	nama		character varying(58) not null,
	isbn		character varying(64),
	penerbit_id	character varying(64)
);

create table perpustakaan.penerbit (
	id 		character varying(64) not null primary key default gen_random_uuid(),
	nama	character varying(58) not null,
	alamat	text
);

create table perpustakaan.anggota (
	id 			  character varying(64)   not null primary key default gen_random_uuid(),
	nomor_ktp	character varying (64)  not null unique,
	nama		  character varying(58)   not null,
	alamat		text
);

create table perpustakaan.transaksi (
	id 				    character varying(64) not null primary key default gen_random_uuid(),
	tgl_transaksi	date                  not null             default now(),
	anggota_id		character varying(64) not null
);

create table perpustakaan.transaksi_detail (
	id 				    character varying(64) not null default gen_random_uuid(),
	transaksi_id	character varying(64) not null,
	buku_id			  character varying(64) not null,
	tgl_kembali		date                  not null
);

create table perpustakaan.penulis (
  id 		  character varying(64) not null primary key default gen_random_uuid(),
	nama    character varying(64) not null,
	alamat  text
);

create table perpustakaan.penulis_buku (
  id 				  character varying(64) not null primary key default gen_random_uuid(),
	buku_id			character varying(64) not null,
	penulis_id  character varying(64) not null
);