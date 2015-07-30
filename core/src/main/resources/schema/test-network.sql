-- Migration SQL that makes the change goes here.
ALTER TABLE network ADD COLUMN data json;

UPDATE network SET data = json_object(array['vpcId', vpcid, 'internetGatewayId', internetgatewayid]) WHERE dtype = 'AwsNetwork';

UPDATE network SET data = json_object(array['addressPrefixCIDR', addressprefixcidr]) WHERE dtype = 'AzureNetwork';

UPDATE network SET data = json_object(array['publicNetId', publicnetid]) WHERE dtype = 'OpenStackNetwork';

ALTER TABLE network ALTER COLUMN data SET DATA TYPE jsonb USING data::jsonb;

ALTER TABLE network ADD COLUMN cloudplatform varchar(31);

UPDATE network SET cloudplatform = 'AWS' WHERE dtype = 'AwsNetwork';
UPDATE network SET cloudplatform = 'AZURE' WHERE dtype = 'AzureNetwork';
UPDATE network SET cloudplatform = 'GCP' WHERE dtype = 'GcpNetwork';
UPDATE network SET cloudplatform = 'OPENSTACK' WHERE dtype = 'OpenStackNetwork';

ALTER TABLE network ALTER COLUMN cloudplatform SET NOT NULL;

ALTER TABLE network DROP COLUMN vpcid;
ALTER TABLE network DROP COLUMN internetgatewayid;
ALTER TABLE network DROP COLUMN addressprefixcidr;
ALTER TABLE network DROP COLUMN publicnetid;
ALTER TABLE network DROP COLUMN dtype;


-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE network ADD COLUMN vpcid varchar(255);
ALTER TABLE network ADD COLUMN internetgatewayid varchar(255);
ALTER TABLE network ADD COLUMN addressprefixcidr varchar(255);
ALTER TABLE network ADD COLUMN publicnetid varchar(255);

ALTER TABLE network ADD COLUMN dtype varchar(31);

Update network SET dtype = 'AwsNetwork' WHERE cloudplatform = 'AWS'
Update network SET dtype = 'AzureNetwork' WHERE cloudplatform = 'AZURE'
Update network SET dtype = 'GcpNetwork' WHERE cloudplatform = 'GCP'
Update network SET dtype = 'OpenStackNetwork' WHERE cloudplatform = 'OPENSTACK'

ALTER TABLE network ALTER COLUMN dtype SET NOT NULL;

UPDATE network
SET vpcid = data->> 'vpcId', internetgatewayid = data->> 'internetGatewayId'
WHERE cloudplatform = 'AWS' and (data->>'vpcId') is not null and (data->>'internetGatewayId') is not null;

UPDATE network
SET addressprefixcidr = data->> 'addressPrefixCIDR'
WHERE cloudplatform = 'AZURE' and (data->>'addressPrefixCIDR') is not null;

UPDATE network
SET addressprefixcidr = data->> 'publicNetId'
WHERE cloudplatform = 'OPENSTACK' and (data->>'publicNetId') is not null;

ALTER TABLE network DROP COLUMN data;
ALTER TABLE network DROP COLUMN cloudplatform;
