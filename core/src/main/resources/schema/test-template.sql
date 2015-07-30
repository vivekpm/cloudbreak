ALTER TABLE template ADD COLUMN data json;

UPDATE template SET data = json_object(array['instanceType', instancetype, 'volumeType', volumetype, 'spotPrice', spotprice::text, 'sshLocation', sshlocation, 'encrypted', encrypted]) WHERE dtype = 'AwsTemplate';
UPDATE template SET data = json_object(array['vmType', vmtype]) WHERE dtype = 'AzureTemplate';
UPDATE template SET data = json_object(array['gcpInstanceType', gcpinstancetype, 'gcpRawDiskType', gcprawdisktype]) WHERE dtype = 'GcpTemplate';
UPDATE template SET data = json_object(array['instanceType',instancetype ]) WHERE dtype = 'OpenStackTemplate';

ALTER TABLE template ALTER COLUMN data SET DATA TYPE jsonb USING data::jsonb;

ALTER TABLE template ADD COLUMN cloudplatform varchar(31);

UPDATE template SET cloudplatform = 'AWS' WHERE dtype = 'AwsTemplate';
UPDATE template SET cloudplatform = 'AZURE' WHERE dtype = 'AzureTemplate';
UPDATE template SET cloudplatform = 'GCP' WHERE dtype = 'GcpTemplate';
UPDATE template SET cloudplatform = 'OPENSTACK' WHERE dtype = 'OpenStackTemplate';

ALTER TABLE template ALTER COLUMN cloudplatform SET NOT NULL;

-- TODO: drops and undo