/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.pipeline.css;

import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.PipelineException;
import com.itextpdf.tool.xml.ProcessObject;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.ctx.ObjectContext;

/**
 * This Pipeline resolves CSS for the Tags it receives in
 * {@link CssResolverPipeline#open(WorkerContext, Tag, ProcessObject)}
 *
 * @author redlab_b
 *
 */
public class CssResolverPipeline extends AbstractPipeline<ObjectContext<CSSResolver>> {

	private CSSResolver resolver;

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.AbstractPipeline#init(com.itextpdf.tool.xml.WorkerContext)
	 */
	@Override
	public Pipeline<?> init(final WorkerContext context) throws PipelineException {
		try {
			ObjectContext<CSSResolver> mc = new ObjectContext<CSSResolver>(resolver.clear());
			context.put(getContextKey(), mc);
			return super.init(context);
		} catch (CssResolverException e) {
			throw new PipelineException(e);
		}
	}
	/**
	 * @param next the next pipeline.
	 * @param cssResolver the {@link CSSResolver} to use in this Pipeline, it
	 *            will be stored in a ThreadLocal variable.
	 */
	public CssResolverPipeline(final CSSResolver cssResolver, final Pipeline<?> next) {
		super(next);
		this.resolver = cssResolver;
	}

	/**
	 *
	 */
	public static final String CSS_RESOLVER = "CSS_RESOLVER";

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#open(com.itextpdf.tool.
	 * xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline<?> open(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
		getLocalContext(context).get().resolveStyles(t);
		return getNext();
	}

	/**
	 * Stores the cssResolver for the calling thread.
	 *
	 * @param resolver the CSSResolver to use.
	 */
	public void setResolver(final CSSResolver resolver) {
		this.resolver = resolver;
	}
}
