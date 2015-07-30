ALTER TABLE credential ADD COLUMN data json;

UPDATE credential SET data = json_object(array['roleArn', rolearn, 'keyPairName', keypairname]) WHERE dtype = 'AwsCredential';
UPDATE credential SET data = json_object(array['subscriptionId', subscriptionid, 'cerFile', cerfile, 'sshCerFile', sshcerfile, 'jks', jks, 'postFix', postfix]) WHERE dtype = 'AzureCredential';
UPDATE credential SET data = json_object(array['serviceAccountId', serviceaccountid, 'serviceAccountPrivateKey', serviceaccountprivatekey, 'projectId', projectid]) WHERE dtype = 'GcpCredential';
UPDATE credential SET data = json_object(array['userName', username, 'password', password, 'tenantName', tenantname, 'endpoint', endpoint]) WHERE dtype = 'OpenStackCredential';

ALTER TABLE credential ALTER COLUMN data SET DATA TYPE jsonb USING data::jsonb;

ALTER TABLE credential ADD COLUMN cloudplatform varchar(31);

UPDATE credential SET cloudplatform = 'AWS' WHERE dtype = 'AwsCredential';
UPDATE credential SET cloudplatform = 'AZURE' WHERE dtype = 'AzureCredential';
UPDATE credential SET cloudplatform = 'GCP' WHERE dtype = 'GcpCredential';
UPDATE credential SET cloudplatform = 'OPENSTACK' WHERE dtype = 'OpenStackCredential';

ALTER TABLE credential ALTER COLUMN cloudplatform SET NOT NULL;

-- TODO: drops and undo